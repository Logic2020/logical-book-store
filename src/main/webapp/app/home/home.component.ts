import { Component, OnInit } from '@angular/core';
import { BookService } from '../entities/book/book.service';
import { InventoryServiceService, InventoryItem } from '../inventory-service.service';
import { BookOrderService, OrderItem, Order } from '../book-order.service';

import { IBook } from 'app/entities/book/book.model';

@Component({
  selector: 'logic-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  books: IBook[] | null = null;
  inventory: InventoryItem[] | null = null;

  constructor(
    private bookService: BookService,
    private inventoryService: InventoryServiceService,
    private bookOrderService: BookOrderService
  ) {}

  ngOnInit(): void {
    this.bookService.query().subscribe(({ body }) => {
      console.log(body);
      this.books = body;
    });

    this.inventoryService.query().subscribe(({ body }) => {
      console.log('inventory', body);
      this.inventory = body;
    });
  }

  orderBooks(): void {
    const newBooks: any = {
      items: [
        {
          bookId: '2000',
          quantity: 5,
        },
        {
          bookId: '3000',
          quantity: 3,
        },
      ],
    };

    this.bookOrderService.orderBooks(newBooks).subscribe(resp => {
      console.log('book Order response', resp);
    });
  }
}
