import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import UserRate from './user-rate.vue';
import UserRateService from './user-rate.service';
import AlertService from '@/shared/alert/alert.service';

type UserRateComponentType = InstanceType<typeof UserRate>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('UserRate Management Component', () => {
    let userRateServiceStub: SinonStubbedInstance<UserRateService>;
    let mountOptions: MountingOptions<UserRateComponentType>['global'];

    beforeEach(() => {
      userRateServiceStub = sinon.createStubInstance<UserRateService>(UserRateService);
      userRateServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          jhiItemCount: true,
          bPagination: true,
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'jhi-sort-indicator': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          userRateService: () => userRateServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        userRateServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(UserRate, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(userRateServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.userRates[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(UserRate, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(userRateServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: UserRateComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(UserRate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        userRateServiceStub.retrieve.reset();
        userRateServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        userRateServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(userRateServiceStub.retrieve.called).toBeTruthy();
        expect(comp.userRates[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        userRateServiceStub.retrieve.reset();
        userRateServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(userRateServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.userRates[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(userRateServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        userRateServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeUserRate();
        await comp.$nextTick(); // clear components

        // THEN
        expect(userRateServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(userRateServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
