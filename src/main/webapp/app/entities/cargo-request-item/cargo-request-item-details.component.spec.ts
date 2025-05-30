import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import CargoRequestItemDetails from './cargo-request-item-details.vue';
import CargoRequestItemService from './cargo-request-item.service';
import AlertService from '@/shared/alert/alert.service';

type CargoRequestItemDetailsComponentType = InstanceType<typeof CargoRequestItemDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const cargoRequestItemSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('CargoRequestItem Management Detail Component', () => {
    let cargoRequestItemServiceStub: SinonStubbedInstance<CargoRequestItemService>;
    let mountOptions: MountingOptions<CargoRequestItemDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      cargoRequestItemServiceStub = sinon.createStubInstance<CargoRequestItemService>(CargoRequestItemService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          cargoRequestItemService: () => cargoRequestItemServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        cargoRequestItemServiceStub.find.resolves(cargoRequestItemSample);
        route = {
          params: {
            cargoRequestItemId: `${123}`,
          },
        };
        const wrapper = shallowMount(CargoRequestItemDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.cargoRequestItem).toMatchObject(cargoRequestItemSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        cargoRequestItemServiceStub.find.resolves(cargoRequestItemSample);
        const wrapper = shallowMount(CargoRequestItemDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
