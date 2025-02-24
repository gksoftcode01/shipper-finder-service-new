<template>
  <div>
    <h2 id="page-heading" data-cy="ShowNumberHistoryHeading">
      <span v-text="t$('shipperFinderApp.showNumberHistory.home.title')" id="show-number-history-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('shipperFinderApp.showNumberHistory.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'ShowNumberHistoryCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-show-number-history"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('shipperFinderApp.showNumberHistory.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && showNumberHistories && showNumberHistories.length === 0">
      <span v-text="t$('shipperFinderApp.showNumberHistory.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="showNumberHistories && showNumberHistories.length > 0">
      <table class="table table-striped" aria-describedby="showNumberHistories">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('createdDate')">
              <span v-text="t$('shipperFinderApp.showNumberHistory.createdDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'createdDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('actionByEncId')">
              <span v-text="t$('shipperFinderApp.showNumberHistory.actionByEncId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'actionByEncId'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('entityType')">
              <span v-text="t$('shipperFinderApp.showNumberHistory.entityType')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'entityType'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('entityEncId')">
              <span v-text="t$('shipperFinderApp.showNumberHistory.entityEncId')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'entityEncId'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="showNumberHistory in showNumberHistories" :key="showNumberHistory.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ShowNumberHistoryView', params: { showNumberHistoryId: showNumberHistory.id } }">{{
                showNumberHistory.id
              }}</router-link>
            </td>
            <td>{{ formatDateShort(showNumberHistory.createdDate) || '' }}</td>
            <td>{{ showNumberHistory.actionByEncId }}</td>
            <td>{{ showNumberHistory.entityType }}</td>
            <td>{{ showNumberHistory.entityEncId }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ShowNumberHistoryView', params: { showNumberHistoryId: showNumberHistory.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ShowNumberHistoryEdit', params: { showNumberHistoryId: showNumberHistory.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(showNumberHistory)"
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
          id="shipperFinderApp.showNumberHistory.delete.question"
          data-cy="showNumberHistoryDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-showNumberHistory-heading"
          v-text="t$('shipperFinderApp.showNumberHistory.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-showNumberHistory"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeShowNumberHistory()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./show-number-history.component.ts"></script>
