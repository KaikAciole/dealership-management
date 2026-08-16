import { api } from "@/src/shared/api/api";
import {
  dealershipResponseSchema,
  pageDealershipResponseSchema,
  type DealershipFormValues,
} from "@/src/features/dealerships/model/schemas/dealership.schema";

export async function fetchDealerships() {
  const response = await api.get("/api/v1/dealerships");
  return pageDealershipResponseSchema.parse(response.data);
}

export async function fetchDealershipById(id: string) {
  const response = await api.get(`/api/v1/dealerships/${id}`);
  return dealershipResponseSchema.parse(response.data);
}

export async function createDealership(payload: DealershipFormValues) {
  const response = await api.post("/api/v1/dealerships", payload);
  return dealershipResponseSchema.parse(response.data);
}

export async function updateDealership(id: string, payload: DealershipFormValues) {
  const response = await api.put(`/api/v1/dealerships/${id}`, payload);
  return dealershipResponseSchema.parse(response.data);
}

export async function toggleDealershipStatus(id: string) {
  const response = await api.patch(`/api/v1/dealerships/${id}/status`);
  return dealershipResponseSchema.parse(response.data);
}

export async function deleteDealership(id: string) {
  await api.delete(`/api/v1/dealerships/${id}`);
}
