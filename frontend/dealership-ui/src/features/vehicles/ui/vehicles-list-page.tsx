"use client";

import { useState } from "react";
import Link from "next/link";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useVehicles } from "@/src/features/vehicles/hooks/use-vehicles";
import { VehicleTable } from "@/src/features/vehicles/ui/vehicle-table";
import { VehicleTableSkeleton } from "@/src/features/vehicles/ui/vehicle-table-skeleton";
import { EmptyState } from "@/src/shared/ui/empty-state";
import { CarFront, Search } from "lucide-react";

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
    <main className="page-shell">
      <section className="rounded-3xl border border-sky-100 bg-gradient-to-r from-slate-900 via-blue-900 to-sky-700 p-6 text-white shadow-xl shadow-blue-900/15 md:p-8">
        <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
          <div>
            <p className="section-kicker">Estoque e operacao</p>
            <h1 className="section-title">Veiculos</h1>
            <p className="section-description">
            Consulte veiculos cadastrados e imagens publicadas no MinIO.
          </p>
          </div>

          <Button asChild className="bg-white text-slate-900 hover:bg-slate-100">
            <Link href="/vehicles/new">Novo Veiculo</Link>
          </Button>
        </div>
      </section>

      <section className="surface-card p-4 lift-on-hover">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
          <Input
            placeholder="Buscar por marca, modelo ou cor..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="h-11 border-slate-200 bg-white pl-10"
          />
        </div>
      </section>

      <section>
        {vehiclesQuery.isLoading && <VehicleTableSkeleton />}

        {vehiclesQuery.isError && (
          <EmptyState
            icon={CarFront}
            title="Falha ao carregar veiculos"
            description="Nao foi possivel consultar o estoque agora. Tente novamente em instantes."
            actionLabel="Recarregar"
            onAction={() => vehiclesQuery.refetch()}
          />
        )}

        {!vehiclesQuery.isLoading && !vehiclesQuery.isError && filteredVehicles.length === 0 && (
          <EmptyState
            icon={CarFront}
            title={searchTerm ? "Nenhum veiculo encontrado" : "Nenhum veiculo cadastrado"}
            description={
              searchTerm
                ? "Refine os termos de busca para localizar os veiculos esperados."
                : "Crie o primeiro veiculo para iniciar a gestao de estoque desta unidade."
            }
          />
        )}

        {!vehiclesQuery.isLoading && !vehiclesQuery.isError && filteredVehicles.length > 0 && (
          <VehicleTable data={filteredVehicles} />
        )}
      </section>
    </main>
  );
}
