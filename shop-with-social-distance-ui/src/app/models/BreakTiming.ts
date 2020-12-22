export class BreakTiming {
  id: number;
  breakFrom: string;
  breakTo: string;
  breakType: string;

  constructor(breakFrom: string, breakTo: string, breakType: string) {
    this.breakFrom = breakFrom;
    this.breakTo = breakTo;
    this.breakType = breakType;
  }
}
