<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="languages">
        <h2 class="jh-entity-heading" data-cy="languagesDetailsHeading">
          <span v-text="t$('shipperFinderApp.languages.detail.title')"></span> {{ languages.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperFinderApp.languages.name')"></span>
          </dt>
          <dd>
            <span>{{ languages.name }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="languages.id"
          :to="{ name: 'LanguagesEdit', params: { languagesId: languages.id } }"
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

<script lang="ts" src="./languages-details.component.ts"></script>
