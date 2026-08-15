"use client";

import Image from "next/image";
import Link from "next/link";

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { type VehicleResponse } from "@/src/features/vehicles/model/schemas/vehicle.schema";
import { formatCurrency } from "@/src/shared/lib/formatters";
import { useDeleteVehicle } from "@/src/features/vehicles/hooks/use-vehicles";
import { Pencil, Trash2 } from "lucide-react";

type VehicleTableProps = {
  data: VehicleResponse[];
};

export function VehicleTable({ data }: VehicleTableProps) {
  const deleteVehicleMutation = useDeleteVehicle();

  const handleDelete = (id: string) => {
    if (confirm("Tem certeza que deseja deletar este veículo?")) {
      deleteVehicleMutation.mutate(id);
    }
  };

  return (
    <div className="overflow-hidden rounded-xl border border-border">
      <table className="w-full border-collapse text-sm">
        <thead className="bg-muted/60 text-left">
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
            <tr key={vehicle.id} className="border-t border-border">
              <td className="px-4 py-3">
                {vehicle.imageUrl ? (
                  <Image
                    src={vehicle.imageUrl}
                    alt={`Foto de ${vehicle.brand} ${vehicle.model}`}
                    width={72}
                    height={48}
                    className="h-12 w-[72px] rounded-md object-cover"
                    unoptimized
                  />
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
                  <Button
                    size="icon"
                    variant="ghost"
                    asChild
                    className="h-8 w-8"
                  >
                    <Link href={`/vehicles/${vehicle.id}/edit`}>
                      <Pencil className="h-4 w-4" />
                    </Link>
                  </Button>
                  <Button
                    size="icon"
                    variant="ghost"
                    onClick={() => handleDelete(vehicle.id)}
                    disabled={deleteVehicleMutation.isPending}
                    className="h-8 w-8 text-destructive hover:text-destructive"
                  >
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
