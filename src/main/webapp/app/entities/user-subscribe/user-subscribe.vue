<template>
  <div>
    <h2 id="page-heading" data-cy="UserSubscribeHeading">
      <span v-text="t$('shipperFinderApp.userSubscribe.home.title')" id="user-subscribe-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('shipperFinderApp.userSubscribe.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'UserSubscribeCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-user-subscribe"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('shipperFinderApp.userSubscribe.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && userSubscribes && userSubscribes.length === 0">
      <span v-text="t$('shipperFinderApp.userSubscribe.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="userSubscribes && userSubscribes.length > 0">
      <table class="table table-striped" aria-describedby="userSubscribes">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fromDate')">
              <span v-text="t$('shipperFinderApp.userSubscribe.fromDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fromDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('toDate')">
              <span v-text="t$('shipperFinderApp.userSubscribe.toDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'toDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('isActive')">
              <span v-text="t$('shipperFinderApp.userSubscribe.isActive')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isActive'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('subscribedUserEncId')">
              <span v-text="t$('shipperFinderApp.userSubscribe.subscribedUserEncId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'subscribedUserEncId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('subscribeType.type')">
              <span v-text="t$('shipperFinderApp.userSubscribe.subscribeType')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'subscribeType.type'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="userSubscribe in userSubscribes" :key="userSubscribe.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'UserSubscribeView', params: { userSubscribeId: userSubscribe.id } }">{{
                userSubscribe.id
              }}</router-link>
            </td>
            <td>{{ formatDateShort(userSubscribe.fromDate) || '' }}</td>
            <td>{{ formatDateShort(userSubscribe.toDate) || '' }}</td>
            <td>{{ userSubscribe.isActive }}</td>
            <td>{{ userSubscribe.subscribedUserEncId }}</td>
            <td>
              <div v-if="userSubscribe.subscribeType">
                <router-link :to="{ name: 'SubscribeTypeView', params: { subscribeTypeId: userSubscribe.subscribeType.id } }">{{
                  userSubscribe.subscribeType.type
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'UserSubscribeView', params: { userSubscribeId: userSubscribe.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'UserSubscribeEdit', params: { userSubscribeId: userSubscribe.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(userSubscribe)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
        <span ref="infiniteScrollEl"></span>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="shipperFinderApp.userSubscribe.delete.question"
          data-cy="userSubscribeDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-userSubscribe-heading" v-text="t$('shipperFinderApp.userSubscribe.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-userSubscribe"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeUserSubscribe()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./user-subscribe.component.ts"></script>
