export interface IEcole {
  id: number;
  nomEcole?: string | null;
  adresseEcole?: string | null;
  telephoneEcole?: number | null;
}

export type NewEcole = Omit<IEcole, 'id'> & { id: null };
