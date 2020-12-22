import { StoreTiming } from './StoreTiming';
import { BreakTiming } from './BreakTiming';
import { StoreHoliday } from './StoreHoliday';

export class Store{
  id: number;
  storeName: string;
  slotDuration: string;
  deliveryInSlot: number;
  storeTimings: Array<StoreTiming>;
  breakTimings: Array<BreakTiming>;
  storeHolidays: Array<StoreHoliday>;
  active: boolean;

  constructor(
    storeName: string = '', slotDuration: string = '30', deliveryInSlot: number = 1,
    storeTimings: Array<StoreTiming> = [],
    breakTimings: Array<BreakTiming> = [],
    storeHolidays: Array<StoreHoliday> = []
  ) {
    this.storeName = storeName;
    this.slotDuration = slotDuration;
    this.deliveryInSlot = deliveryInSlot;
    this.storeTimings = storeTimings;
    this.breakTimings = breakTimings;
    this.storeHolidays = storeHolidays;
  }
}
