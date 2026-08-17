"use client";

import Image from "next/image";
import Link from "next/link";
import { useState } from "react";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { type VehicleResponse } from "@/src/features/vehicles/model/schemas/vehicle.schema";
import { formatCurrency } from "@/src/shared/lib/formatters";
import { useDeleteVehicle } from "@/src/features/vehicles/hooks/use-vehicles";
import { Pencil, Trash2 } from "lucide-react";
import {
  AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription,
  AlertDialogFooter, AlertDialogHeader, AlertDialogTitle,
} from "@/components/ui/alert-dialog";

type VehicleTableProps = { data: VehicleResponse[]; };

export function VehicleTable({ data }: VehicleTableProps) {
  const deleteVehicleMutation = useDeleteVehicle();
  const [vehicleToDelete, setVehicleToDelete] = useState<string | null>(null);

  const confirmDelete = () => {
    if (vehicleToDelete) {
      deleteVehicleMutation.mutate(vehicleToDelete);
      setVehicleToDelete(null);
    }
  };

  return (
    <div className="overflow-hidden rounded-2xl border border-slate-200 bg-white/90 shadow-sm backdrop-blur">
      <table className="w-full border-collapse text-sm">
        <thead className="bg-slate-50 text-left">
          <tr>
            <th className="px-4 py-3 font-medium">Foto</th>
            <th className="px-4 py-3 font-medium">Marca/Modelo</th>
            <th className="px-4 py-3 font-medium">Combustivel</th>
            <th className="px-4 py-3 font-medium">Ano</th>
            <th className="px-4 py-3 font-medium">Preco</th>
            <th className="px-4 py-3 font-medium">Cor externa</th>
            <th className="px-4 py-3 font-medium">Acoes</th>
          </tr>
        </thead>
        <tbody>
          {data.map((vehicle) => (
            <tr key={vehicle.id} className="border-t border-slate-100 hover:bg-slate-50/70">
              <td className="px-4 py-3">
                {vehicle.imageUrl ? (
                  <Image src={vehicle.imageUrl} alt={`Foto de ${vehicle.brand} ${vehicle.model}`} width={72} height={48} className="h-12 w-[72px] rounded-md object-cover" unoptimized />
                ) : (
                  <Badge variant="outline">Sem imagem</Badge>
                )}
              </td>
              <td className="px-4 py-3 font-medium">{`${vehicle.brand} ${vehicle.model}`}</td>
              <td className="px-4 py-3">{vehicle.fuelType}</td>
              <td className="px-4 py-3">{vehicle.manufactureYear ?? "-"}</td>
              <td className="px-4 py-3">{formatCurrency(vehicle.price)}</td>
              <td className="px-4 py-3">{vehicle.externalColor ?? "-"}</td>
              <td className="px-4 py-3">
                <div className="flex items-center gap-2">
                  <Button size="icon" variant="ghost" asChild className="h-8 w-8" aria-label="Editar Veículo">
                    <Link href={`/vehicles/${vehicle.id}/edit`}><Pencil className="h-4 w-4" /></Link>
                  </Button>
                  <Button size="icon" variant="ghost" onClick={() => setVehicleToDelete(vehicle.id)} disabled={deleteVehicleMutation.isPending} className="h-8 w-8 text-destructive hover:bg-red-50 hover:text-destructive" aria-label="Remover Veículo">
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <AlertDialog open={!!vehicleToDelete} onOpenChange={() => setVehicleToDelete(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Excluir veículo?</AlertDialogTitle>
            <AlertDialogDescription>Essa ação não pode ser desfeita. Isso removerá o veículo permanentemente do estoque.</AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel disabled={deleteVehicleMutation.isPending}>Cancelar</AlertDialogCancel>
            <AlertDialogAction onClick={confirmDelete} className="bg-destructive text-destructive-foreground hover:bg-destructive/90">
              {deleteVehicleMutation.isPending ? "Excluindo..." : "Sim, excluir"}
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
}