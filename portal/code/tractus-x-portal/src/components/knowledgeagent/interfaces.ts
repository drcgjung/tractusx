export interface SparqlResult {
  head: Head,
  results: Results,
  currentPage?: number
  totalPages?: number
}

export interface Head {
 vars: string[]
}

export interface Results {
 bindings: object[]
}