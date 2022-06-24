import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'logic-books',
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.scss'],
})
export class BooksComponent implements OnInit {
  ngOnInit(): void {
    console.log('on init');
  }
}
