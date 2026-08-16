export interface AddressRequest {
  cep: string;
  street: string;
  number: string;
  city: string;
  state: string;
  neighborhood: string;
}

export interface AddressResponse {
  cep: string;
  street: string;
  number: string;
  neighborhood: string;
  city: string;
  state: string;
}

export interface VehicleRequest {
  brand: string;
  model: string;
  fuelType: string;
  color: string;
  manufactureYear: number;
  chassis: string;
  price: number;
  externalColor: string;
  dealershipId: string;
}

export interface VehicleResponse {
  id: string;
  brand: string;
  model: string;
  fuelType: string;
  color: string;
  manufactureYear: number;
  chassis: string;
  price: number;
  externalColor: string;
  imageUrl: string;
  dealershipId: string;
}

export interface DealershipRequest {
  corporateName: string;
  cnpj: string;
  address: AddressRequest;
}

export interface DealershipResponse {
  id: string;
  corporateName: string;
  cnpj: string;
  address: AddressResponse;
  foundationDate: string;
  isActive: boolean;
}

export interface PageableObject {
  paged: boolean;
  pageNumber: number;
  pageSize: number;
  offset: number;
  sort: {
    sorted: boolean;
    empty: boolean;
    unsorted: boolean;
  };
  unpaged: boolean;
}

export interface PageVehicleResponse {
  totalPages: number;
  totalElements: number;
  pageable: PageableObject;
  size: number;
  content: VehicleResponse[];
  number: number;
  sort: unknown;
  first: boolean;
  last: boolean;
  numberOfElements: number;
  empty: boolean;
}

export interface PageDealershipResponse {
  totalPages: number;
  totalElements: number;
  pageable: PageableObject;
  size: number;
  content: DealershipResponse[];
  number: number;
  sort: unknown;
  first: boolean;
  last: boolean;
  numberOfElements: number;
  empty: boolean;
}
