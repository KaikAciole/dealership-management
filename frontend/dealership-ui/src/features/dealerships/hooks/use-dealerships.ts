"use client";

import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

import {
  createDealership,
  deleteDealership,
  fetchDealerships,
  fetchDealershipById,
  updateDealership,
} from "@/src/features/dealerships/api/dealerships.service";
import { dealershipsQueryKeys } from "@/src/features/dealerships/api/dealerships.query-keys";
import { toast } from "sonner";

export function useDealerships() {
  return useQuery({
    queryKey: dealershipsQueryKeys.all,
    queryFn: fetchDealerships,
  });
}

export function useDealership(id: string) {
  return useQuery({
    queryKey: [...dealershipsQueryKeys.all, id] as const,
    queryFn: () => fetchDealershipById(id),
    enabled: Boolean(id),
  });
}

export function useCreateDealership() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: createDealership,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: dealershipsQueryKeys.all });
    },
  });
}

export function useUpdateDealership() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, payload }: { id: string; payload: Parameters<typeof updateDealership>[1] }) =>
      updateDealership(id, payload),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: dealershipsQueryKeys.all });
    },
  });
}

export function useDeleteDealership() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: deleteDealership,
    onSuccess: () => {
      toast.success("Concessionária deletada com sucesso!");
      queryClient.invalidateQueries({ queryKey: dealershipsQueryKeys.all });
    },
    onError: () => {
      toast.error("Erro ao deletar concessionária.");
    },
  });
}

