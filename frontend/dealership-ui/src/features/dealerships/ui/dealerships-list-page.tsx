"use client";

import { useState } from "react";
import Link from "next/link";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useDealerships } from "@/src/features/dealerships/hooks/use-dealerships";
import { DealershipTable } from "@/src/features/dealerships/ui/dealership-table";
import { DealershipTableSkeleton } from "@/src/features/dealerships/ui/dealership-table-skeleton";
import { Search } from "lucide-react";

export function DealershipsListPage() {
  const dealershipsQuery = useDealerships();
  const [searchTerm, setSearchTerm] = useState("");

  const dealerships = dealershipsQuery.data?.content ?? [];

  const filteredDealerships = dealerships.filter((dealership) => {
    const searchLower = searchTerm.toLowerCase();
    return (
      dealership.corporateName.toLowerCase().includes(searchLower) ||
      dealership.cnpj.toLowerCase().includes(searchLower)
    );
  });

  return (
    <main className="mx-auto flex w-full max-w-7xl flex-1 flex-col gap-6 px-4 py-8 md:px-8">
      <section className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
        <div>
          <h1 className="text-3xl font-semibold tracking-tight">Concessionarias</h1>
          <p className="text-sm text-muted-foreground">
            Gerencie o cadastro e consulte os dados enriquecidos por OpenCNPJ.
          </p>
        </div>

        <Button asChild>
          <Link href="/dealerships/new">Nova Concessionaria</Link>
        </Button>
      </section>

      <section className="flex gap-2">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
          <Input
            placeholder="Buscar por razão social ou CNPJ..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="pl-10"
          />
        </div>
      </section>

      <section>
        {dealershipsQuery.isLoading ? (
          <DealershipTableSkeleton />
        ) : (
          <DealershipTable data={filteredDealerships} />
        )}
      </section>
    </main>
  );
}
