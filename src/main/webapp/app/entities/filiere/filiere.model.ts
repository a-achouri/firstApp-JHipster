import { IEcole } from 'app/entities/ecole/ecole.model';

export interface IFiliere {
  id: number;
  codeFiliere?: number | null;
  nomFiliere?: string | null;
  typeFiliere?: string | null;
  ecole?: Pick<IEcole, 'id'> | null;
}

export type NewFiliere = Omit<IFiliere, 'id'> & { id: null };
