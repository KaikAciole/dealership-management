"use client";

import { useState } from "react";
import Link from "next/link";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useVehicles } from "@/src/features/vehicles/hooks/use-vehicles";
import { VehicleTable } from "@/src/features/vehicles/ui/vehicle-table";
import { VehicleTableSkeleton } from "@/src/features/vehicles/ui/vehicle-table-skeleton";
import { Search } from "lucide-react";

export function VehiclesListPage() {
  const vehiclesQuery = useVehicles();
  const [searchTerm, setSearchTerm] = useState("");

  const vehicles = vehiclesQuery.data?.content ?? [];

  const filteredVehicles = vehicles.filter((vehicle) => {
    const searchLower = searchTerm.toLowerCase();
    return (
      vehicle.brand.toLowerCase().includes(searchLower) ||
      vehicle.model.toLowerCase().includes(searchLower) ||
      vehicle.color.toLowerCase().includes(searchLower)
    );
  });

  return (
    <main className="mx-auto flex w-full max-w-7xl flex-1 flex-col gap-6 px-4 py-8 md:px-8">
      <section className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
        <div>
          <h1 className="text-3xl font-semibold tracking-tight">Veiculos</h1>
          <p className="text-sm text-muted-foreground">
            Consulte veiculos cadastrados e imagens publicadas no MinIO.
          </p>
        </div>

        <Button asChild>
          <Link href="/vehicles/new">Novo Veiculo</Link>
        </Button>
      </section>

      <section className="flex gap-2">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
          <Input
            placeholder="Buscar por marca, modelo ou cor..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="pl-10"
          />
        </div>
      </section>

      <section>
        {vehiclesQuery.isLoading ? (
          <VehicleTableSkeleton />
        ) : (
          <VehicleTable data={filteredVehicles} />
        )}
      </section>
    </main>
  );
}
