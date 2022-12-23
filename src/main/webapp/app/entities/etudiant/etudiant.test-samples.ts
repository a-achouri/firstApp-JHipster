import { IEtudiant, NewEtudiant } from './etudiant.model';

export const sampleWithRequiredData: IEtudiant = {
  id: 99461,
};

export const sampleWithPartialData: IEtudiant = {
  id: 30199,
  age: 13317,
  email: 'Alfonzo.Torp@gmail.com',
};

export const sampleWithFullData: IEtudiant = {
  id: 95256,
  codeEtudiant: 49231,
  nom: 'Future',
  prenom: 'United Wyoming system',
  age: 99294,
  email: 'Katrina.Jones1@yahoo.com',
};

export const sampleWithNewData: NewEtudiant = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
