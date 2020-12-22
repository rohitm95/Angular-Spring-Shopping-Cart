export class StoreHoliday {
  id: number;
  date: Date;
  holiday: string;

  constructor(date: Date, holiday: string) {
    this.date = date;
    this.holiday = holiday;
  }
}
