export class StoreTiming {
  id: number;
  day: string;
  deliveryStartAt: string;
  deliveryEndAt: string;
  weaklyOff: boolean;

  constructor(
    day: string, deliveryStartAt: string, deliveryEndAt: string, weaklyOff: boolean
  ) {
    this.day = day;
    this.deliveryStartAt = deliveryStartAt;
    this.deliveryEndAt = deliveryEndAt;
    this.weaklyOff = weaklyOff;
  }
}
