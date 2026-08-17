"use client";

import { useMutation, useQuery, useQueryClient, keepPreviousData } from "@tanstack/react-query";
import {
  createVehicle,
  deleteVehicle,
  fetchVehicles,
  fetchVehicleById,
  updateVehicle,
  uploadVehicleImage,
} from "@/src/features/vehicles/api/vehicles.service";
import { vehiclesQueryKeys } from "@/src/features/vehicles/api/vehicles.query-keys";
import { toast } from "sonner";

export function useVehicles(page = 0, size = 10) {
  return useQuery({
    queryKey: [...vehiclesQueryKeys.all, page, size],
    queryFn: () => fetchVehicles(page, size),
    placeholderData: keepPreviousData,
  });
}

export function useVehicle(id: string) {
  return useQuery({
    queryKey: [...vehiclesQueryKeys.all, id] as const,
    queryFn: () => fetchVehicleById(id),
    enabled: Boolean(id),
  });
}

export function useCreateVehicle() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: createVehicle,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: vehiclesQueryKeys.all });
    },
  });
}

export function useUpdateVehicle() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, payload }: { id: string; payload: Parameters<typeof updateVehicle>[1] }) =>
      updateVehicle(id, payload),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: vehiclesQueryKeys.all });
    },
  });
}

export function useUploadVehicleImage() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ vehicleId, file }: { vehicleId: string; file: File }) =>
      uploadVehicleImage(vehicleId, file),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: vehiclesQueryKeys.all });
    },
  });
}

export function useDeleteVehicle() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: deleteVehicle,
    onSuccess: () => {
      toast.success("Veículo deletado com sucesso!");
      queryClient.invalidateQueries({ queryKey: vehiclesQueryKeys.all });
    },
    onError: () => {
      toast.error("Erro ao deletar veículo.");
    },
  });
}