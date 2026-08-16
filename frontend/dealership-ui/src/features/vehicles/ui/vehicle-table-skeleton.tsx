import { Skeleton } from "@/components/ui/skeleton";

export function VehicleTableSkeleton() {
  return (
    <div className="surface-card space-y-3 p-4">
      <div className="grid grid-cols-7 gap-3">
        <Skeleton className="h-6" />
        <Skeleton className="h-6 col-span-2" />
        <Skeleton className="h-6" />
        <Skeleton className="h-6" />
        <Skeleton className="h-6" />
        <Skeleton className="h-6" />
      </div>
      <Skeleton className="h-12 w-full" />
      <Skeleton className="h-12 w-full" />
      <Skeleton className="h-12 w-full" />
    </div>
  );
}
