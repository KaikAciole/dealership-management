"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { useDealerships } from "@/src/features/dealerships/hooks/use-dealerships";
import { DealershipTableSkeleton } from "@/src/features/dealerships/ui/dealership-table-skeleton";
import { VehicleForm } from "@/src/features/vehicles/ui/vehicle-form";

export function VehicleCreatePage() {
  const router = useRouter();
  const dealershipsQuery = useDealerships();
  const dealerships = dealershipsQuery.data?.content ?? [];

  return (
    <main className="page-shell max-w-3xl">
      <section className="rounded-3xl border border-sky-100 bg-gradient-to-r from-slate-900 via-blue-900 to-sky-700 p-6 text-white shadow-xl shadow-blue-900/15 md:p-8">
        <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
          <div>
            <p className="section-kicker">Novo ativo de estoque</p>
            <h1 className="section-title">Novo Veiculo</h1>
            <p className="section-description">Cadastre um veiculo e realize upload de imagem com multipart/form-data.</p>
          </div>
          <Button variant="outline" className="border-white/40 bg-white/10 text-white hover:bg-white/20" asChild>
            <Link href="/vehicles">Voltar para listagem</Link>
          </Button>
        </div>
      </section>

      {dealershipsQuery.isLoading ? (
        <DealershipTableSkeleton />
      ) : (
        <VehicleForm dealerships={dealerships} onSuccess={() => router.push("/vehicles")} />
      )}
    </main>
  );
}