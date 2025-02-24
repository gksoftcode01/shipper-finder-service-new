<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="item">
        <h2 class="jh-entity-heading" data-cy="itemDetailsHeading">
          <span v-text="t$('shipperFinderApp.item.detail.title')"></span> {{ item.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('shipperFinderApp.item.name')"></span>
          </dt>
          <dd>
            <span>{{ item.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.item.nameEn')"></span>
          </dt>
          <dd>
            <span>{{ item.nameEn }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.item.nameAr')"></span>
          </dt>
          <dd>
            <span>{{ item.nameAr }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.item.nameFr')"></span>
          </dt>
          <dd>
            <span>{{ item.nameFr }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.item.nameDe')"></span>
          </dt>
          <dd>
            <span>{{ item.nameDe }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.item.nameUrdu')"></span>
          </dt>
          <dd>
            <span>{{ item.nameUrdu }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.item.isActive')"></span>
          </dt>
          <dd>
            <span>{{ item.isActive }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.item.defaultUOM')"></span>
          </dt>
          <dd>
            <span>{{ item.defaultUOM }}</span>
          </dd>
          <dt>
            <span v-text="t$('shipperFinderApp.item.itemType')"></span>
          </dt>
          <dd>
            <div v-if="item.itemType">
              <router-link :to="{ name: 'ItemTypeView', params: { itemTypeId: item.itemType.id } }">{{ item.itemType.name }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="item.id" :to="{ name: 'ItemEdit', params: { itemId: item.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./item-details.component.ts"></script>
