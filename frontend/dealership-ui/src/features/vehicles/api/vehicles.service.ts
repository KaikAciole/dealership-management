import { api } from "@/src/shared/api/api";
import {
  pageVehicleResponseSchema,
  type VehicleFormValues,
  vehicleResponseSchema,
} from "@/src/features/vehicles/model/schemas/vehicle.schema";

export async function fetchVehicles() {
  const response = await api.get("/api/v1/vehicles");
  return pageVehicleResponseSchema.parse(response.data);
}

export async function fetchVehicleById(id: string) {
  const response = await api.get(`/api/v1/vehicles/${id}`);
  return vehicleResponseSchema.parse(response.data);
}

export async function createVehicle(payload: VehicleFormValues) {
  const response = await api.post("/api/v1/vehicles", payload);
  return vehicleResponseSchema.parse(response.data);
}

export async function updateVehicle(id: string, payload: VehicleFormValues) {
  const response = await api.put(`/api/v1/vehicles/${id}`, payload);
  return vehicleResponseSchema.parse(response.data);
}

export async function uploadVehicleImage(vehicleId: string, file: File) {
  const formData = new FormData();
  formData.append("file", file);

  const response = await api.patch(`/api/v1/vehicles/${vehicleId}/image`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });

  return vehicleResponseSchema.parse(response.data);
}

export async function deleteVehicle(id: string) {
  await api.delete(`/api/v1/vehicles/${id}`);
}
