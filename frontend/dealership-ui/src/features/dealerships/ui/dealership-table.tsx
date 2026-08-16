"use client";

import Link from "next/link";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { type DealershipResponse } from "@/src/features/dealerships/model/schemas/dealership.schema";
import { formatCnpj, formatDate } from "@/src/shared/lib/formatters";
import {
  useDeleteDealership,
  useToggleDealershipStatus,
} from "@/src/features/dealerships/hooks/use-dealerships";
import { Pencil, Power, Trash2 } from "lucide-react";

type DealershipTableProps = {
  data: DealershipResponse[];
};

export function DealershipTable({ data }: DealershipTableProps) {
  const deleteDealershipMutation = useDeleteDealership();
  const toggleStatusMutation = useToggleDealershipStatus();

  const handleDelete = (id: string) => {
    if (confirm("Tem certeza que deseja deletar esta concessionária?")) {
      deleteDealershipMutation.mutate(id);
    }
  };

  return (
    <div className="overflow-hidden rounded-2xl border border-slate-200 bg-white/90 shadow-sm backdrop-blur">
      <table className="w-full border-collapse text-sm">
        <thead className="bg-slate-50 text-left">
          <tr>
            <th className="px-4 py-3 font-medium">Razao social</th>
            <th className="px-4 py-3 font-medium">CNPJ</th>
            <th className="px-4 py-3 font-medium">Fundacao</th>
            <th className="px-4 py-3 font-medium">Status CNPJ</th>
            <th className="px-4 py-3 font-medium">Cidade/UF</th>
            <th className="px-4 py-3 font-medium">Acoes</th>
          </tr>
        </thead>
        <tbody>
          {data.map((dealership) => (
            <tr key={dealership.id} className="border-t border-slate-100 hover:bg-slate-50/70">
              <td className="px-4 py-3 font-medium">{dealership.corporateName}</td>
              <td className="px-4 py-3">{formatCnpj(dealership.cnpj)}</td>
              <td className="px-4 py-3">{formatDate(dealership.foundationDate)}</td>
              <td className="px-4 py-3">
                {dealership.isActive === true && <Badge variant="success">Ativo</Badge>}
                {dealership.isActive === false && <Badge variant="muted">Inativo</Badge>}
                {dealership.isActive == null && <Badge variant="outline">Nao informado</Badge>}
              </td>
              <td className="px-4 py-3">
                {dealership.address.city} / {dealership.address.state}
              </td>
              <td className="px-4 py-3">
                <div className="flex items-center gap-2">
                  <Button
                    size="icon"
                    variant="ghost"
                    asChild
                    className="h-8 w-8"
                  >
                    <Link href={`/dealerships/${dealership.id}/edit`}>
                      <Pencil className="h-4 w-4" />
                    </Link>
                  </Button>
                  <Button
                    size="icon"
                    variant="ghost"
                    onClick={() => toggleStatusMutation.mutate(dealership.id)}
                    disabled={toggleStatusMutation.isPending}
                    className="h-8 w-8 text-sky-700 hover:bg-sky-50 hover:text-sky-800"
                    title={dealership.isActive ? "Desativar" : "Ativar"}
                  >
                    <Power className="h-4 w-4" />
                  </Button>
                  <Button
                    size="icon"
                    variant="ghost"
                    onClick={() => handleDelete(dealership.id)}
                    disabled={deleteDealershipMutation.isPending}
                    className="h-8 w-8 text-destructive hover:bg-red-50 hover:text-destructive"
                  >
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
