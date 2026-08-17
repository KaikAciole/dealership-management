"use client";

import Link from "next/link";
import { useParams, useRouter } from "next/navigation";

import { Button } from "@/components/ui/button";
import { useDealerships } from "@/src/features/dealerships/hooks/use-dealerships";
import { DealershipTableSkeleton } from "@/src/features/dealerships/ui/dealership-table-skeleton";
import { useVehicle } from "@/src/features/vehicles/hooks/use-vehicles";
import { VehicleForm } from "@/src/features/vehicles/ui/vehicle-form";

export function VehicleEditPage() {
  const router = useRouter();
  const params = useParams();
  const id = params.id as string; 

  const vehicleQuery = useVehicle(id);
  const dealershipsQuery = useDealerships();

  if (vehicleQuery.isLoading || dealershipsQuery.isLoading) {
    return (
      <main className="mx-auto flex w-full max-w-3xl flex-1 flex-col gap-6 px-4 py-8 md:px-8">
        <DealershipTableSkeleton />
      </main>
    );
  }

  return (
    <main className="page-shell max-w-3xl">
      <section className="mb-6 rounded-3xl border border-sky-100 bg-gradient-to-r from-slate-900 via-blue-900 to-sky-700 p-6 text-white shadow-xl shadow-blue-900/15 md:p-8">
        <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
          <div>
            <p className="section-kicker">Edicao de estoque</p>
            <h1 className="section-title">Editar Veiculo</h1>
            <p className="section-description">
              Atualize os dados comerciais e tecnicos do veiculo, incluindo imagem quando necessario.
            </p>
          </div>

          <Button variant="outline" className="border-white/40 bg-white/10 text-white hover:bg-white/20" asChild>
            <Link href="/vehicles">Voltar para listagem</Link>
          </Button>
        </div>
      </section>

      <VehicleForm
        initialData={vehicleQuery.data}
        dealerships={dealershipsQuery.data?.content ?? []}
        onSuccess={() => router.push("/vehicles")}
      />
    </main>
  );
}