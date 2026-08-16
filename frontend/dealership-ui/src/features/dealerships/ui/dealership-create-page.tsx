import Link from "next/link";

import { Button } from "@/components/ui/button";
import { DealershipForm } from "@/src/features/dealerships/ui/dealership-form";

export function DealershipCreatePage() {
  return (
    <main className="page-shell max-w-3xl">
      <section className="rounded-3xl border border-sky-100 bg-gradient-to-r from-slate-900 via-blue-900 to-sky-700 p-6 text-white shadow-xl shadow-blue-900/15 md:p-8">
        <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
        <div>
            <p className="section-kicker">Nova unidade</p>
            <h1 className="section-title">Nova Concessionaria</h1>
            <p className="section-description">
            Preencha os dados e use o CEP para autocomplete de endereco via ViaCEP.
          </p>
        </div>

          <Button variant="outline" className="border-white/40 bg-white/10 text-white hover:bg-white/20" asChild>
            <Link href="/dealerships">Voltar para listagem</Link>
          </Button>
        </div>
      </section>

      <DealershipForm />
    </main>
  );
}
