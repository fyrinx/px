import type { Component } from 'solid-js';
import File from './components/File'
import TopTen from './components/TopTen';
import Network from './components/Network';
import Summary from './components/Summary';
import EpisodeList from './components/Episodes';
import Nextweek from './components/Nextweek';
const App: Component = () => {

  return (
    <div>
        Hello
      <File/>
      For all these, click the left button before the right button.
      The values from clicking the left button are obtained in console.log.
      <Nextweek/>
      <TopTen/>
      <Network/>
      <Summary/>
      <EpisodeList/>
    </div>
  );
};
// const file=() => {
//   return (
//     <div>
//         <form>
//           Upload file here.
//           <input type="file" accept=".txt"/>
//           <button type="submit">Upload</button>
//         </form>
//     </div>
//   );
// }
export default App;
