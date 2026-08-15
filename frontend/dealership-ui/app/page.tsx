import Link from "next/link";

import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";

export default function Home() {
  return (
    <main className="mx-auto flex w-full max-w-7xl flex-1 flex-col gap-8 px-4 py-10 md:px-8">
      <section className="max-w-3xl space-y-4">
        <p className="text-sm font-medium uppercase tracking-[0.2em] text-muted-foreground">
          Dealership Management
        </p>
        <h1 className="text-4xl font-semibold tracking-tight md:text-5xl">
          Frontend para gestão de concessionárias e veículos.
        </h1>
        <p className="text-base text-muted-foreground md:text-lg">
          Navegue pelas rotas dedicadas para listar, criar e editar concessionárias e veículos com
          React Query, Zod, React Hook Form e upload MinIO.
        </p>
      </section>

      <section className="grid gap-4 md:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>Concessionárias</CardTitle>
            <CardDescription>
              Cadastre, edite e consulte dados enriquecidos com OpenCNPJ e ViaCEP.
            </CardDescription>
          </CardHeader>
          <CardContent className="flex gap-3">
            <Button asChild>
              <Link href="/dealerships">Abrir listagem</Link>
            </Button>
            <Button variant="outline" asChild>
              <Link href="/dealerships/new">Nova concessionária</Link>
            </Button>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Veículos</CardTitle>
            <CardDescription>
              Gere veículos com upload de imagem, vínculo com concessionária e edição completa.
            </CardDescription>
          </CardHeader>
          <CardContent className="flex gap-3">
            <Button asChild>
              <Link href="/vehicles">Abrir listagem</Link>
            </Button>
            <Button variant="outline" asChild>
              <Link href="/vehicles/new">Novo veículo</Link>
            </Button>
          </CardContent>
        </Card>
      </section>
    </main>
  );
}
