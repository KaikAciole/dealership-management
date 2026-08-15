import { z } from "zod";

export const sortStateSchema = z.object({
  sorted: z.boolean(),
  empty: z.boolean(),
  unsorted: z.boolean(),
});

export const pageableObjectSchema = z.object({
  paged: z.boolean(),
  pageNumber: z.number().int(),
  pageSize: z.number().int(),
  offset: z.number().int(),
  sort: z.array(
    z.object({
      ascending: z.boolean(),
      descending: z.boolean(),
      direction: z.string(),
      ignoreCase: z.boolean(),
      nullHandling: z.string(),
      property: z.string(),
    })
  ),
  unpaged: z.boolean(),
});

export function createPageSchema<T extends z.ZodTypeAny>(contentSchema: T) {
  return z.object({
    totalPages: z.number().int(),
    totalElements: z.number().int(),
    pageable: pageableObjectSchema,
    size: z.number().int(),
    content: z.array(contentSchema),
    number: z.number().int(),
    sort: z.array(
      z.object({
        ascending: z.boolean(),
        descending: z.boolean(),
        direction: z.string(),
        ignoreCase: z.boolean(),
        nullHandling: z.string(),
        property: z.string(),
      })
    ),
    first: z.boolean(),
    last: z.boolean(),
    numberOfElements: z.number().int(),
    empty: z.boolean(),
  });
}
