"use client";

import Link from "next/link";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { type DealershipResponse } from "@/src/features/dealerships/model/schemas/dealership.schema";
import { formatCnpj, formatDate } from "@/src/shared/lib/formatters";
import { useDeleteDealership } from "@/src/features/dealerships/hooks/use-dealerships";
import { Pencil, Trash2 } from "lucide-react";

type DealershipTableProps = {
  data: DealershipResponse[];
};

export function DealershipTable({ data }: DealershipTableProps) {
  const deleteDeleteMutation = useDeleteDealership();

  const handleDelete = (id: string) => {
    if (confirm("Tem certeza que deseja deletar esta concessionária?")) {
      deleteDeleteMutation.mutate(id);
    }
  };

  return (
    <div className="overflow-hidden rounded-xl border border-border">
      <table className="w-full border-collapse text-sm">
        <thead className="bg-muted/60 text-left">
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
            <tr key={dealership.id} className="border-t border-border">
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
                    onClick={() => handleDelete(dealership.id)}
                    disabled={deleteDeleteMutation.isPending}
                    className="h-8 w-8 text-destructive hover:text-destructive"
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
