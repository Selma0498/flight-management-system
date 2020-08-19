export interface IPlane {
  id?: number;
  manufacturer?: string;
  modelNumber?: string;
  registrationNumber?: string;
}

export class Plane implements IPlane {
  constructor(public id?: number, public manufacturer?: string, public modelNumber?: string, public registrationNumber?: string) {}
}
