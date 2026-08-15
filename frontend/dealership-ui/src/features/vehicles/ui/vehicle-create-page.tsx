"use client";

import Link from "next/link";

import { Button } from "@/components/ui/button";
import { useDealerships } from "@/src/features/dealerships/hooks/use-dealerships";
import { DealershipTableSkeleton } from "@/src/features/dealerships/ui/dealership-table-skeleton";
import { VehicleForm } from "@/src/features/vehicles/ui/vehicle-form";

export function VehicleCreatePage() {
  const dealershipsQuery = useDealerships();
  const dealerships = dealershipsQuery.data?.content ?? [];

  return (
    <main className="mx-auto flex w-full max-w-3xl flex-1 flex-col gap-6 px-4 py-8 md:px-8">
      <section className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
        <div>
          <h1 className="text-3xl font-semibold tracking-tight">Novo Veiculo</h1>
          <p className="text-sm text-muted-foreground">
            Cadastre um veiculo e realize upload de imagem com multipart/form-data.
          </p>
        </div>

        <Button variant="outline" asChild>
          <Link href="/vehicles">Voltar para listagem</Link>
        </Button>
      </section>

      {dealershipsQuery.isLoading ? (
        <DealershipTableSkeleton />
      ) : (
        <VehicleForm dealerships={dealerships} />
      )}
    </main>
  );
}
