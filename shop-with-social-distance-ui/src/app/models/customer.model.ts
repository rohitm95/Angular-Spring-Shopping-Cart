export class Customer {
    id: number;
    username: string;
    firstName: string;
    lastName: string;
    emailId: string;
    mobileNumber: string;
    role: string;
    isActive: boolean;
    category: string;
    afd_purchase_limit: number;
    nonAFD_purchase_limit: number;
    gender:string;
    store:string;
}

export class CustomerToAddUpdate {

    constructor(){
        this.role = new CustomerRoleToAddUpdate();
        this.store = new Store();
    }

    id: number;
    username: string;
    firstName: string;
    lastName: string;
    emailId: string;
    mobileNumber: string;
    role:CustomerRoleToAddUpdate;
    isActive: boolean;
    category: string;
    afd_purchase_limit: number;
    nonAFD_purchase_limit: number;
    dateOfJoin: string;
    gender: string;
    store:Store;
}

export class CustomerRoleToAddUpdate {
    id: number;
    name: string;
}


export class Store {
    id: number;
    name: string;
}
