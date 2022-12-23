import { IEcole, NewEcole } from './ecole.model';

export const sampleWithRequiredData: IEcole = {
  id: 90152,
};

export const sampleWithPartialData: IEcole = {
  id: 32099,
  nomEcole: 'platforms Marketing',
};

export const sampleWithFullData: IEcole = {
  id: 46244,
  nomEcole: 'Turkey',
  adresseEcole: 'Paradigm Interactions',
  telephoneEcole: 65021,
};

export const sampleWithNewData: NewEcole = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
