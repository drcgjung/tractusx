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

export interface Utterance {
  phrases: string[],
  pauses: number[]
}

export interface Skill {
  nick:string,
  name: string,
  match(string): string,
  summarize(SparqlResult): Utterance
}

