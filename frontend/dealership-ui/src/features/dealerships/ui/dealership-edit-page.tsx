"use client";

import Link from "next/link";

import { Button } from "@/components/ui/button";

import { DealershipForm } from "@/src/features/dealerships/ui/dealership-form";
import { DealershipTableSkeleton } from "@/src/features/dealerships/ui/dealership-table-skeleton";
import { useDealership } from "@/src/features/dealerships/hooks/use-dealerships";

type DealershipEditPageProps = {
  id: string;
};

export function DealershipEditPage({ id }: DealershipEditPageProps) {
  const dealershipQuery = useDealership(id);

  if (dealershipQuery.isLoading) {
    return (
      <main className="mx-auto flex w-full max-w-3xl flex-1 flex-col gap-6 px-4 py-8 md:px-8">
        <DealershipTableSkeleton />
      </main>
    );
  }

  return (
    <main className="page-shell max-w-3xl">
      <section className="rounded-3xl border border-sky-100 bg-gradient-to-r from-slate-900 via-blue-900 to-sky-700 p-6 text-white shadow-xl shadow-blue-900/15 md:p-8">
        <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
          <div>
            <p className="section-kicker">Edicao de unidade</p>
            <h1 className="section-title">Editar Concessionaria</h1>
            <p className="section-description">
              Atualize os dados cadastrais e altere o status ativo/inativo sem sair da pagina.
            </p>
          </div>

          <Button variant="outline" className="border-white/40 bg-white/10 text-white hover:bg-white/20" asChild>
            <Link href="/dealerships">Voltar para listagem</Link>
          </Button>
        </div>
      </section>

      <DealershipForm initialData={dealershipQuery.data} />
    </main>
  );
}
