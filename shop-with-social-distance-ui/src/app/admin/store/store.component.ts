import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-store',
  templateUrl: './store.component.html',
  styleUrls: ['./store.component.scss']
})
export class StoreComponent implements OnInit {

  value = 0;
  constructor() { }

  ngOnInit(): void {
  }

  changeTab(index){
    this.value = index;
  }

}
