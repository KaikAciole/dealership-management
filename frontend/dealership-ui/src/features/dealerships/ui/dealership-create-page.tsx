import Link from "next/link";

import { Button } from "@/components/ui/button";
import { DealershipForm } from "@/src/features/dealerships/ui/dealership-form";

export function DealershipCreatePage() {
  return (
    <main className="mx-auto flex w-full max-w-3xl flex-1 flex-col gap-6 px-4 py-8 md:px-8">
      <section className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
        <div>
          <h1 className="text-3xl font-semibold tracking-tight">Nova Concessionaria</h1>
          <p className="text-sm text-muted-foreground">
            Preencha os dados e use o CEP para autocomplete de endereco via ViaCEP.
          </p>
        </div>

        <Button variant="outline" asChild>
          <Link href="/dealerships">Voltar para listagem</Link>
        </Button>
      </section>

      <DealershipForm />
    </main>
  );
}
