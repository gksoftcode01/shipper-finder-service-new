<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="subscribeTypeDetail">
        <h2 class="jh-entity-heading" data-cy="subscribeTypeDetailDetailsHeading">
          <span v-text="t$('shipperFinderApp.subscribeTypeDetail.detail.title')"></span> {{ subscribeTypeDetail.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperFinderApp.subscribeTypeDetail.price')"></span>
          </dt>
          <dd>
            <span>{{ subscribeTypeDetail.price }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.subscribeTypeDetail.maxTrip')"></span>
          </dt>
          <dd>
            <span>{{ subscribeTypeDetail.maxTrip }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.subscribeTypeDetail.maxItems')"></span>
          </dt>
          <dd>
            <span>{{ subscribeTypeDetail.maxItems }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.subscribeTypeDetail.maxRequest')"></span>
          </dt>
          <dd>
            <span>{{ subscribeTypeDetail.maxRequest }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.subscribeTypeDetail.maxNumberView')"></span>
          </dt>
          <dd>
            <span>{{ subscribeTypeDetail.maxNumberView }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.subscribeTypeDetail.subscribeType')"></span>
          </dt>
          <dd>
            <div v-if="subscribeTypeDetail.subscribeType">
              <router-link :to="{ name: 'SubscribeTypeView', params: { subscribeTypeId: subscribeTypeDetail.subscribeType.id } }">{{
                subscribeTypeDetail.subscribeType.nameEn
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="subscribeTypeDetail.id"
          :to="{ name: 'SubscribeTypeDetailEdit', params: { subscribeTypeDetailId: subscribeTypeDetail.id } }"
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

<script lang="ts" src="./subscribe-type-detail-details.component.ts"></script>
