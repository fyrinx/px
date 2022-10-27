import type { Component } from 'solid-js';
import File from './components/File'
import TopTen from './components/Topten';

const App: Component = () => {

  return (
    <div>
        Hello
      <File/>
      <TopTen/>
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
