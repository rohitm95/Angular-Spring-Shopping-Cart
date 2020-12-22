export class Inventory {
    id: number;
    itemNumber: string;
    group: string;
    itemName: string;
    price: number;
    stock: number;
    category: string;
    lowStockIndicator: string;
    toBeSoldOneItemPerOrder: string;
    volumePerItem: number;
    monthlyQuotaPerUser: string;
    itemType: string;
    yearlyQuotaPerUser: string;    
    noOfItemsOrderded: number;
    Weight_volume_per_item :number;
}

export class InventoryToAddUpdate {
    constructor(){
        this.store = new StoreModel();
    }
    id: number;
    itemNumber: string;
    group: string;
    itemName: string;
    price: number;
    stock: number;
    category: string;
    lowStockIndicator: boolean;
    toBeSoldOneItemPerOrder: boolean;
    volumePerItem: number;
    monthlyQuotaPerUser: string;
    itemType: string;
    yearlyQuotaPerUser: string;    
    noOfItemsOrderded: number;
    Weight_volume_per_item :number;
    store: StoreModel;
}

export class StoreModel{
    id:number;
    name:string;
}