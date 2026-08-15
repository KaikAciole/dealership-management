import { Skeleton } from "@/components/ui/skeleton";

export function DealershipTableSkeleton() {
  return (
    <div className="space-y-3 rounded-xl border border-border p-4">
      <Skeleton className="h-5 w-40" />
      <Skeleton className="h-10 w-full" />
      <Skeleton className="h-10 w-full" />
      <Skeleton className="h-10 w-full" />
    </div>
  );
}
