<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="cargoRequest">
        <h2 class="jh-entity-heading" data-cy="cargoRequestDetailsHeading">
          <span v-text="t$('shipperFinderApp.cargoRequest.detail.title')"></span> {{ cargoRequest.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperFinderApp.cargoRequest.createDate')"></span>
          </dt>
          <dd>
            <span v-if="cargoRequest.createDate">{{ formatDateLong(cargoRequest.createDate) }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.cargoRequest.validUntil')"></span>
          </dt>
          <dd>
            <span v-if="cargoRequest.validUntil">{{ formatDateLong(cargoRequest.validUntil) }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.cargoRequest.status')"></span>
          </dt>
          <dd>
            <span v-text="t$('shipperFinderApp.CargoRequestStatus.' + cargoRequest.status)"></span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.cargoRequest.isNegotiable')"></span>
          </dt>
          <dd>
            <span>{{ cargoRequest.isNegotiable }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.cargoRequest.budget')"></span>
          </dt>
          <dd>
            <span>{{ cargoRequest.budget }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.cargoRequest.createdByEncId')"></span>
          </dt>
          <dd>
            <span>{{ cargoRequest.createdByEncId }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.cargoRequest.takenByEncId')"></span>
          </dt>
          <dd>
            <span>{{ cargoRequest.takenByEncId }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.cargoRequest.encId')"></span>
          </dt>
          <dd>
            <span>{{ cargoRequest.encId }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.cargoRequest.fromCountry')"></span>
          </dt>
          <dd>
            <div v-if="cargoRequest.fromCountry">
              <router-link :to="{ name: 'CountryView', params: { countryId: cargoRequest.fromCountry.id } }">{{
                cargoRequest.fromCountry.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.cargoRequest.toCountry')"></span>
          </dt>
          <dd>
            <div v-if="cargoRequest.toCountry">
              <router-link :to="{ name: 'CountryView', params: { countryId: cargoRequest.toCountry.id } }">{{
                cargoRequest.toCountry.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.cargoRequest.fromState')"></span>
          </dt>
          <dd>
            <div v-if="cargoRequest.fromState">
              <router-link :to="{ name: 'StateProvinceView', params: { stateProvinceId: cargoRequest.fromState.id } }">{{
                cargoRequest.fromState.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.cargoRequest.toState')"></span>
          </dt>
          <dd>
            <div v-if="cargoRequest.toState">
              <router-link :to="{ name: 'StateProvinceView', params: { stateProvinceId: cargoRequest.toState.id } }">{{
                cargoRequest.toState.name
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="cargoRequest.id"
          :to="{ name: 'CargoRequestEdit', params: { cargoRequestId: cargoRequest.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./cargo-request-details.component.ts"></script>
