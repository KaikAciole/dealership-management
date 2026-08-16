"use client";

import Link from "next/link";
import { useMemo, useState } from "react";
import { Building2, CarFront, ChevronRight, Gauge, MapPin } from "lucide-react";

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { useDealerships } from "@/src/features/dealerships/hooks/use-dealerships";
import { useVehicles } from "@/src/features/vehicles/hooks/use-vehicles";
import { EmptyState } from "@/src/shared/ui/empty-state";

export function HomeShowcase() {
  const dealershipsQuery = useDealerships();
  const vehiclesQuery = useVehicles();

  const dealerships = useMemo(
    () => dealershipsQuery.data?.content ?? [],
    [dealershipsQuery.data?.content]
  );
  const vehicles = useMemo(
    () => vehiclesQuery.data?.content ?? [],
    [vehiclesQuery.data?.content]
  );

  const [selectedDealershipId, setSelectedDealershipId] = useState<string>("");

  const isLoading = dealershipsQuery.isLoading || vehiclesQuery.isLoading;

  const selectedDealership = useMemo(
    () => dealerships.find((dealership) => dealership.id === selectedDealershipId) ?? dealerships[0],
    [dealerships, selectedDealershipId]
  );

  const dealershipVehicles = useMemo(() => {
    if (!selectedDealership) {
      return [];
    }

    return vehicles.filter((vehicle) => vehicle.dealershipId === selectedDealership.id);
  }, [selectedDealership, vehicles]);

  if (isLoading) {
    return (
      <main className="page-shell">
        <section className="surface-card p-8">
          <Skeleton className="h-7 w-48" />
          <Skeleton className="mt-4 h-12 w-full max-w-2xl" />
          <Skeleton className="mt-3 h-6 w-full max-w-xl" />
        </section>
        <section className="grid gap-4 md:grid-cols-3">
          <Skeleton className="h-40 w-full" />
          <Skeleton className="h-40 w-full" />
          <Skeleton className="h-40 w-full" />
        </section>
      </main>
    );
  }

  if (!selectedDealership && dealerships.length === 0) {
    return (
      <main className="page-shell">
        <EmptyState
          icon={Building2}
          title="Nenhuma concessionaria disponivel"
          description="Cadastre ao menos uma concessionaria para desbloquear os dashboards e o estoque associado."
        />
      </main>
    );
  }

  return (
    <main className="mx-auto flex w-full max-w-7xl flex-1 flex-col gap-8 px-4 py-10 md:px-8">
      <section className="overflow-hidden rounded-3xl border border-sky-100 bg-gradient-to-br from-slate-900 via-blue-900 to-sky-700 p-8 text-white shadow-2xl shadow-blue-900/20">
        <div className="grid gap-8 lg:grid-cols-[1.6fr_1fr]">
          <div className="space-y-4">
            <Badge className="bg-white/20 text-white hover:bg-white/20">Concessionaria Premium</Badge>
            <h1 className="text-4xl font-semibold leading-tight tracking-tight md:text-5xl">
              Plataforma de gestao para aceleracao comercial e excelencia operacional.
            </h1>
            <p className="max-w-2xl text-sm text-sky-100 md:text-base">
              Uma interface moderna para gerir concessionarias, veiculos, imagens e indicadores de
              performance com foco em qualidade de dados e produtividade da equipe.
            </p>
            <div className="flex flex-wrap gap-3">
              <Button asChild className="bg-white text-slate-900 hover:bg-slate-100">
                <Link href="/dealerships">
                  Gerenciar concessionarias
                  <ChevronRight className="h-4 w-4" />
                </Link>
              </Button>
              <Button asChild variant="outline" className="border-white/40 bg-white/10 text-white hover:bg-white/20">
                <Link href="/vehicles">Ver estoque de veiculos</Link>
              </Button>
            </div>
          </div>

          <div className="grid gap-3 rounded-2xl border border-white/20 bg-white/10 p-4 backdrop-blur">
            <div className="rounded-xl bg-white/15 p-4">
              <p className="text-xs uppercase tracking-wide text-sky-100">Concessionarias ativas</p>
              <p className="mt-2 text-3xl font-semibold">{dealerships.filter((d) => d.isActive).length}</p>
            </div>
            <div className="rounded-xl bg-white/15 p-4">
              <p className="text-xs uppercase tracking-wide text-sky-100">Veiculos cadastrados</p>
              <p className="mt-2 text-3xl font-semibold">{vehicles.length}</p>
            </div>
            <div className="rounded-xl bg-white/15 p-4">
              <p className="text-xs uppercase tracking-wide text-sky-100">Conversao media</p>
              <p className="mt-2 text-3xl font-semibold">38%</p>
            </div>
          </div>
        </div>
      </section>

      <section className="grid gap-4 md:grid-cols-3">
        <Card className="border-sky-100 bg-white/85 backdrop-blur lift-on-hover">
          <CardHeader className="pb-3">
            <CardDescription>Operacao</CardDescription>
            <CardTitle className="flex items-center gap-2 text-lg">
              <Building2 className="h-5 w-5 text-sky-600" />
              Governanca de concessionarias
            </CardTitle>
          </CardHeader>
          <CardContent className="text-sm text-slate-600">
            Centralize dados de matriz, filiais e status de atividade em uma unica experiencia.
          </CardContent>
        </Card>

        <Card className="border-sky-100 bg-white/85 backdrop-blur lift-on-hover">
          <CardHeader className="pb-3">
            <CardDescription>Comercial</CardDescription>
            <CardTitle className="flex items-center gap-2 text-lg">
              <CarFront className="h-5 w-5 text-sky-600" />
              Inventario de veiculos
            </CardTitle>
          </CardHeader>
          <CardContent className="text-sm text-slate-600">
            Monitore preco, disponibilidade e qualidade visual de cada veiculo no estoque.
          </CardContent>
        </Card>

        <Card className="border-sky-100 bg-white/85 backdrop-blur lift-on-hover">
          <CardHeader className="pb-3">
            <CardDescription>Performance</CardDescription>
            <CardTitle className="flex items-center gap-2 text-lg">
              <Gauge className="h-5 w-5 text-sky-600" />
              Indicadores de ponta a ponta
            </CardTitle>
          </CardHeader>
          <CardContent className="text-sm text-slate-600">
            Tome decisoes com base em dados operacionais e comerciais em tempo real.
          </CardContent>
        </Card>
      </section>

      <section className="grid gap-4 lg:grid-cols-[1.3fr_2fr]">
        <Card className="border-sky-100 bg-white/90 backdrop-blur">
          <CardHeader>
            <CardTitle className="text-xl">Concessionarias</CardTitle>
            <CardDescription>Selecione uma unidade para visualizar os veiculos associados.</CardDescription>
          </CardHeader>
          <CardContent className="grid gap-2">
            {dealerships.map((dealership) => {
              const isSelected = selectedDealership?.id === dealership.id;

              return (
                <button
                  key={dealership.id}
                  type="button"
                  onClick={() => setSelectedDealershipId(dealership.id)}
                  className={[
                    "rounded-xl border px-3 py-3 text-left transition-all duration-200",
                    isSelected
                      ? "border-sky-300 bg-sky-50 shadow-sm"
                      : "border-slate-200 bg-white hover:-translate-y-0.5 hover:border-sky-200 hover:bg-slate-50 hover:shadow-sm",
                  ].join(" ")}
                >
                  <div className="flex items-center justify-between gap-2">
                    <p className="font-medium text-slate-800">{dealership.corporateName}</p>
                    {dealership.isActive ? <Badge variant="success">Ativa</Badge> : <Badge variant="muted">Inativa</Badge>}
                  </div>
                  <p className="mt-1 flex items-center gap-1 text-xs text-slate-500">
                    <MapPin className="h-3.5 w-3.5" />
                    {dealership.address.city} / {dealership.address.state}
                  </p>
                </button>
              );
            })}
          </CardContent>
        </Card>

        <Card className="border-sky-100 bg-white/90 backdrop-blur">
          <CardHeader>
            <CardTitle className="text-xl">Veiculos da concessionaria selecionada</CardTitle>
            <CardDescription>
              {selectedDealership
                ? `Exibindo estoque de ${selectedDealership.corporateName}`
                : "Selecione uma concessionaria para visualizar o estoque."}
            </CardDescription>
          </CardHeader>
          <CardContent>
            {dealershipVehicles.length > 0 ? (
              <div className="grid gap-3 sm:grid-cols-2">
                {dealershipVehicles.map((vehicle) => (
                  <div
                    key={vehicle.id}
                    className="rounded-xl border border-slate-200 bg-white p-4 shadow-sm transition-all duration-200 hover:-translate-y-0.5 hover:border-sky-200 hover:shadow"
                  >
                    <p className="text-sm font-semibold text-slate-900">{vehicle.brand} {vehicle.model}</p>
                    <p className="mt-1 text-xs text-slate-500">{vehicle.fuelType} | {vehicle.color}</p>
                    <p className="mt-3 text-sm font-medium text-sky-700">{vehicle.price ? `R$ ${vehicle.price.toLocaleString("pt-BR")}` : "Preco sob consulta"}</p>
                  </div>
                ))}
              </div>
            ) : (
              <div className="rounded-xl border border-dashed border-slate-300 bg-slate-50 p-6 text-center text-sm text-slate-500">
                Nenhum veiculo associado para a concessionaria selecionada.
              </div>
            )}
          </CardContent>
        </Card>
      </section>
    </main>
  );
}
