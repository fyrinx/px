/**
 * Generated by orval v6.10.2 🍺
 * Do not edit manually.
 * My shiny API
 * My shiny API description
 * OpenAPI spec version: v0.0.1
 */
import type { NetworkFromApi } from './networkFromApi';
import type { ShowTime } from './showTime';

export interface ShowFromApi {
  id?: number;
  episodeCount?: number;
  releasedEpisodeCount?: number;
  rating?: number;
  network?: NetworkFromApi;
  genres?: string[];
  days?: string[];
  name?: string;
  summary?: string;
  ended?: string;
  schedule?: ShowTime;
}
