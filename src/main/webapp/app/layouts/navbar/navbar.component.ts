import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SessionStorageService } from 'ngx-webstorage';

import { VERSION } from 'app/app.constants';

import { EntityNavbarItems } from 'app/entities/entity-navbar-items';

@Component({
  selector: 'logic-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = true;
  openAPIEnabled?: boolean;
  version = '';
  entitiesNavbarItems: any[] = [];

  constructor(
    private sessionStorageService: SessionStorageService,
    private router: Router
  ) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }

  ngOnInit(): void {
    this.entitiesNavbarItems = EntityNavbarItems;

  }

  changeLanguage(languageKey: string): void {
    this.sessionStorageService.store('locale', languageKey);
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }



  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }
}
