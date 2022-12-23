import { IFiliere, NewFiliere } from './filiere.model';

export const sampleWithRequiredData: IFiliere = {
  id: 65746,
};

export const sampleWithPartialData: IFiliere = {
  id: 78914,
  codeFiliere: 43232,
  typeFiliere: 'Flat Illinois',
};

export const sampleWithFullData: IFiliere = {
  id: 58719,
  codeFiliere: 34024,
  nomFiliere: 'back-end enhance Bedfordshire',
  typeFiliere: 'Analyst',
};

export const sampleWithNewData: NewFiliere = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
