<template>
  <div class="about">
    <h1>Alert Suppression & Processing</h1>
    &nbsp;
    <b-card style="max-width: 60vw; margin: auto">
      <b-card-body>
        <p style="text-align: left">
          Primarily this is an exercise in using new found knowledge on Spring
          Boot Utilizing Spring Web / Rest and Websockets and the Vuejs
          javascript framework
        </p>
        <p style="text-align: left">
          Essentially the service uses a ruleset based on regex pattern matching
          to allow a published event to be published as an alert or (still being
          looked into) to suppress or otherwise reduce duplicate alerts
        </p>
        <p style="text-align: left">
          The final aspect of this is to have on github a published body of work
          showing new found Spring Boot skills in a more complex setup.
        </p>
      </b-card-body>
    </b-card>
    &nbsp;
    <b-card style="max-width: 60vw; margin: auto">
      <b-card-body>
        <p style="text-align: left">
          As mentioned the alert filtering rules based using regex for the
          message matching and to provide the alert contents via a rudimentary
          templating system
        </p>
        <p style="text-align: left">
          The regex named fields can be used in the correlation Id and Output
          fields of a rule (place the named fields inside a pair of curly braces
        </p>
        <p style="text-align: left">
          eg
          <code>{<code>{message}</code>}</code>
        </p>

        <p style="text-align: left">
          Lastly this project is not completely done as some of the planed alert
          filtering is not implemented.
        </p>
      </b-card-body>
    </b-card>
  </div>
</template>
