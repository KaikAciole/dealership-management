"use client";

import { useState } from "react";
import Link from "next/link";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useDealerships } from "@/src/features/dealerships/hooks/use-dealerships";
import { DealershipTable } from "@/src/features/dealerships/ui/dealership-table";
import { DealershipTableSkeleton } from "@/src/features/dealerships/ui/dealership-table-skeleton";
import { EmptyState } from "@/src/shared/ui/empty-state";
import { Building2, Search, ChevronLeft, ChevronRight } from "lucide-react";

export function DealershipsListPage() {
  const [page, setPage] = useState(0);
  const dealershipsQuery = useDealerships(page);
  const [searchTerm, setSearchTerm] = useState("");

  const pageData = dealershipsQuery.data;
  const dealerships = pageData?.content ?? [];

  const filteredDealerships = dealerships.filter((dealership) => {
    const searchLower = searchTerm.toLowerCase();
    return (
      dealership.corporateName.toLowerCase().includes(searchLower) ||
      dealership.cnpj.toLowerCase().includes(searchLower)
    );
  });

  return (
    <main className="page-shell">
      <section className="rounded-3xl border border-sky-100 bg-gradient-to-r from-slate-900 via-blue-900 to-sky-700 p-6 text-white shadow-xl shadow-blue-900/15 md:p-8">
        <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
          <div>
            <p className="section-kicker">Backoffice de unidades</p>
            <h1 className="section-title">Concessionarias</h1>
            <p className="section-description">
              Gerencie o cadastro e consulte os dados enriquecidos por OpenCNPJ.
            </p>
          </div>

          <Button asChild className="bg-white text-slate-900 hover:bg-slate-100">
            <Link href="/dealerships/new">Nova Concessionaria</Link>
          </Button>
        </div>
      </section>

      <section className="surface-card p-4 lift-on-hover">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
          <Input
            placeholder="Buscar por razão social ou CNPJ..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="h-11 border-slate-200 bg-white pl-10"
          />
        </div>
      </section>

      <section className="space-y-4">
        {dealershipsQuery.isLoading && <DealershipTableSkeleton />}

        {dealershipsQuery.isError && (
          <EmptyState
            icon={Building2}
            title="Falha ao carregar concessionarias"
            description="Nao foi possivel carregar a listagem agora. Tente novamente em instantes."
            actionLabel="Recarregar"
            onAction={() => dealershipsQuery.refetch()}
          />
        )}

        {!dealershipsQuery.isLoading && !dealershipsQuery.isError && filteredDealerships.length === 0 && (
          <EmptyState
            icon={Building2}
            title={searchTerm ? "Nenhuma concessionaria encontrada" : "Nenhuma concessionaria cadastrada"}
            description={
              searchTerm
                ? "Ajuste os filtros para encontrar os registros esperados."
                : "Comece criando a primeira concessionaria para habilitar os fluxos operacionais."
            }
          />
        )}

        {!dealershipsQuery.isLoading && !dealershipsQuery.isError && filteredDealerships.length > 0 && (
          <>
            <DealershipTable data={filteredDealerships} />

            {/* Controles de Paginação */}
            <div className="flex items-center justify-between px-2 pt-2">
              <p className="text-sm text-slate-500">
                Página <span className="font-medium">{page + 1}</span> de <span className="font-medium">{pageData?.totalPages || 1}</span>
              </p>
              <div className="flex items-center gap-2">
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => setPage((p) => Math.max(0, p - 1))}
                  disabled={page === 0 || dealershipsQuery.isFetching}
                >
                  <ChevronLeft className="mr-1 h-4 w-4" /> Anterior
                </Button>
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => setPage((p) => p + 1)}
                  disabled={pageData?.last || (pageData?.totalPages ? page >= pageData.totalPages - 1 : true) || dealershipsQuery.isFetching}
                >
                  Próxima <ChevronRight className="ml-1 h-4 w-4" />
                </Button>
              </div>
            </div>
          </>
        )}
      </section>
    </main>
  );
}