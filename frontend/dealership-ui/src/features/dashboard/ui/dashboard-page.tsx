"use client";

import { DealershipForm } from "@/src/features/dealerships/ui/dealership-form";
import { DealershipTable } from "@/src/features/dealerships/ui/dealership-table";
import { DealershipTableSkeleton } from "@/src/features/dealerships/ui/dealership-table-skeleton";
import { useDealerships } from "@/src/features/dealerships/hooks/use-dealerships";
import { VehicleForm } from "@/src/features/vehicles/ui/vehicle-form";
import { VehicleTable } from "@/src/features/vehicles/ui/vehicle-table";
import { VehicleTableSkeleton } from "@/src/features/vehicles/ui/vehicle-table-skeleton";
import { useVehicles } from "@/src/features/vehicles/hooks/use-vehicles";

export function DashboardPage() {
  const dealershipsQuery = useDealerships();
  const vehiclesQuery = useVehicles();

  const dealerships = dealershipsQuery.data?.content ?? [];
  const vehicles = vehiclesQuery.data?.content ?? [];

  return (
    <main className="mx-auto flex w-full max-w-7xl flex-1 flex-col gap-8 px-4 py-8 md:px-8">
      <section className="space-y-2">
        <h1 className="text-3xl font-semibold tracking-tight">
          Gestao de Concessionarias e Veiculos
        </h1>
        <p className="text-muted-foreground">
          Fluxo completo com React Query, Zod, React Hook Form, ViaCEP e upload MinIO.
        </p>
      </section>

      <section className="grid gap-8 lg:grid-cols-2">
        <DealershipForm />
        <VehicleForm dealerships={dealerships} />
      </section>

      <section className="grid gap-8">
        <div className="space-y-3">
          <h2 className="text-xl font-semibold">Concessionarias</h2>
          {dealershipsQuery.isLoading ? (
            <DealershipTableSkeleton />
          ) : (
            <DealershipTable data={dealerships} />
          )}
        </div>

        <div className="space-y-3">
          <h2 className="text-xl font-semibold">Veiculos</h2>
          {vehiclesQuery.isLoading ? (
            <VehicleTableSkeleton />
          ) : (
            <VehicleTable data={vehicles} />
          )}
        </div>
      </section>
    </main>
  );
}
