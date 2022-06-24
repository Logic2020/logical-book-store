export interface IBook {
  id?: string;
  title?: string;
  authors?: string[];
  year?: number;
  price?: number;
}

export class Book implements IBook {
  constructor(public id: string, public title: string, public authors: string[], public year: number, public price: number) {}
}

export function getBookIdentifier(book: IBook): string | undefined {
  return book.id;
}
