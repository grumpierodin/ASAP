import Vue from "vue";
import SockJS from "sockjs-client";
import globalBus from "../event-bus";
import $store from "../store";

class RealTimeClient {
  constructor() {
    this.serverUrl = null;
    this.socket = null;
    // If the client is authenticated through real time connection or not
    this.authenticated = false;
    this.loggedOut = false;
    this.$bus = new Vue();
    this.subscribeQueue = {
      /* channel: [handler1, handler2] */
    };
    this.unsubscribeQueue = {
      /* channel: [handler1, handler2] */
    };
  }
  init(serverUrl) {
    if (this.authenticated) {
      console.warn("[RealTimeClient] WS connection already authenticated.");
      return;
    }
    $store.commit("setRealtimeMessage", "[RealTimeClient] Initializing");
    this.serverUrl = serverUrl;
    this.connect();
  }
  logout() {
    $store.commit("setRealtimeMessage", "[RealTimeClient] Logging out");
    this.subscribeQueue = {};
    this.unsubscribeQueue = {};
    this.authenticated = false;
    this.loggedOut = true;
    this.socket && this.socket.close();
    this.socket = null;
  }
  connect() {
    $store.commit(
      "setRealtimeMessage",
      "[RealTimeClient] Connecting to " + this.serverUrl
    );
    this.socket = new SockJS(
      this.serverUrl + "?token=" + $store.getters.getToken
    );
    this.socket.onopen = () => {
      // Once the connection established, always set the client as authenticated
      this.authenticated = true;
      this._onConnected();
    };
    this.socket.onmessage = (event) => {
      event.preventDefault();
      event.stopPropagation();
      this._onMessageReceived(event);
    };
    this.socket.onerror = (error) => {
      this._onSocketError(error);
    };
    this.socket.onclose = (event) => {
      this._onClosed(event);
    };
  }
  subscribe(channel, handler) {
    if (!this._isConnected()) {
      this._addToSubscribeQueue(channel, handler);
      return;
    }
    const message = {
      action: "subscribe",
      channel,
    };
    this._send(message);
    this.$bus.$on(this._channelEvent(channel), handler);
    $store.commit(
      "setRealtimeMessage",
      "[RealTimeClient] Subscribed to channel " + channel
    );
  }
  unsubscribe(channel, handler) {
    // Already logged out, no need to unsubscribe
    if (this.loggedOut) {
      return;
    }

    if (!this._isConnected()) {
      this._addToUnsubscribeQueue(channel, handler);
      return;
    }
    const message = {
      action: "unsubscribe",
      channel,
    };
    this._send(message);
    this.$bus.$off(this._channelEvent(channel), handler);
    $store.commit(
      "setRealtimeMessage",
      "[RealTimeClient] Unsubscribed from channel " + channel
    );
  }
  _isConnected() {
    return this.socket && this.socket.readyState === SockJS.OPEN;
  }
  _onConnected() {
    globalBus.$emit("RealTimeClient.connected");
    $store.commit("setRealtimeMessage", "[RealTimeClient] Connected");

    // Handle subscribe and unsubscribe queue
    this._processQueues();
  }
  _onMessageReceived(event) {
    event.preventDefault();
    event.stopPropagation();
    const message = JSON.parse(event.data);
    console.log(message);
    $store.commit("setRealtimeMessage", "[RealTimeClient] Received message");

    if (message.channel) {
      this.$bus.$emit(this._channelEvent(message.channel), message.payload);
    }
  }
  _send(message) {
    this.socket.send(JSON.stringify(message));
  }
  _onSocketError(error) {
    console.log("socket error");
    console.log(error);
    $store.commit(
      "setRealtimeMessage",
      "[RealTimeClient] Socket error " + error
    );
  }
  _onClosed(event) {
    $store.commit(
      "setRealtimeMessage",
      "[RealTimeClient] Received close event",
      event
    );
    if (this.loggedOut) {
      // Manually logged out, no need to reconnect
      $store.commit("setRealtimeMessage", "[RealTimeClient] Logged out");
      globalBus.$emit("RealTimeClient.loggedOut");
    } else {
      // Temporarily disconnected, attempt reconnect
      $store.commit("setRealtimeMessage", "[RealTimeClient] Disconnected");
      globalBus.$emit("RealTimeClient.disconnected");

      setTimeout(() => {
        $store.commit("setRealtimeMessage", "[RealTimeClient] Reconnecting");
        globalBus.$emit("RealTimeClient.reconnecting");
        this.connect();
      }, 1000);
    }
  }
  _channelEvent(channel) {
    return "channel:" + channel;
  }
  _processQueues() {
    $store.commit(
      "setRealtimeMessage",
      "[RealTimeClient] Processing subscribe/unsubscribe queues"
    );

    // Process subscribe queue
    const subscribeChannels = Object.keys(this.subscribeQueue);
    subscribeChannels.forEach((channel) => {
      const handlers = this.subscribeQueue[channel];
      handlers.forEach((handler) => {
        this.subscribe(channel, handler);
        this._removeFromQueue(this.subscribeQueue, channel, handler);
      });
    });

    // Process unsubscribe queue
    const unsubscribeChannels = Object.keys(this.unsubscribeQueue);
    unsubscribeChannels.forEach((channel) => {
      const handlers = this.unsubscribeQueue[channel];
      handlers.forEach((handler) => {
        this.unsubscribe(channel, handler);
        this._removeFromQueue(this.unsubscribeQueue, channel, handler);
      });
    });
  }
  _addToSubscribeQueue(channel, handler) {
    $store.commit(
      "setRealtimeMessage",
      "[RealTimeClient] Adding channel subscribe to queue. Channel: " + channel
    );
    // To make sure the unsubscribe won't be sent out to the server
    this._removeFromQueue(this.unsubscribeQueue, channel, handler);
    const handlers = this.subscribeQueue[channel];
    if (!handlers) {
      this.subscribeQueue[channel] = [handler];
    } else {
      handlers.push(handler);
    }
  }
  _addToUnsubscribeQueue(channel, handler) {
    $store.commit(
      "setRealtimeMessage",
      "[RealTimeClient] Adding channel unsubscribe to queue. Channel: " +
        channel
    );
    // To make sure the subscribe won't be sent out to the server
    this._removeFromQueue(this.subscribeQueue, channel, handler);
    const handlers = this.unsubscribeQueue[channel];
    if (!handlers) {
      this.unsubscribeQueue[channel] = [handler];
    } else {
      handlers.push(handlers);
    }
  }
  _removeFromQueue(queue, channel, handler) {
    const handlers = queue[channel];
    if (handlers) {
      let index = handlers.indexOf(handler);
      if (index > -1) {
        handlers.splice(index, 1);
      }
    }
  }
}

export default new RealTimeClient();
